import urllib.request
import requests
import re
import json
from bs4 import BeautifulSoup

def getWinRate(row):
    # <tr onclick="DoNav('/hero/detail/antimage')" style="cursor: pointer;">
    #     <td style="width: 30%">
    #         <img class="hero-img-list" src="http://cdn.dota2.com/apps/dota2/images/heroes/antimage_hphover.png"/>
    #         <span class="hero-name-list">
    #             敌法师
    #         </span>
    #     </td>
    #     <td style="width: 40%">
    #         <div style="height: 10px">
    #             51.34%
    #         </div>
    #         <div class="segment segment-green" style="width:51.3352012325%;">
    #         </div>
    #     </td>
    #     <td style="width: 30%">
    #         <div style="height: 10px">
    #             3,307,816
    #         </div>
    #         <div class="segment segment-gold" style="width:52.4077205239%;">
    #         </div>
    #     </td>
    # </tr>
    link = re.findall('\'(.+)\'', row["onclick"])[0]
    heroName = link.split('/')[3]
    winRate = float(re.findall('width:(.*)%', row.find("div", {"class":"segment segment-green"})["style"])[0]) / 100

    return heroName, winRate

def getAntiComb(heroName):
    matchUpAnti = {}
    matchUpComb = {}
    antiUrl = 'http://www.dotamax.com/hero/detail/match_up_anti/' + heroName + '/'
    combUrl = 'http://www.dotamax.com/hero/detail/match_up_comb/' + heroName + '/'
    antiData = getUrlSoup(antiUrl).find("table", {"class", "table table-hover table-striped sortable table-list table-thead-left"})
    combData = getUrlSoup(combUrl).find("table", {"class", "table table-hover table-striped sortable table-list table-thead-left"})
    for row in antiData.findAll("tr"):
        # <tr>
        #     <td>
        #         <a href="/hero/detail/puck">
        #             <img class="hero-img-list" src="http://cdn.dota2.com/apps/dota2/images/heroes/puck_hphover.png"/>
        #         </a>
        #         <span class="hero-name-list">
        #             帕克
        #         </span>
        #     </td>
        #     <td>
        #         <div style="height: 10px">
        #             -4.55%
        #         </div>
        #         <div class="segment segment-red" style="width:23.2102229564%;">
        #         </div>
        #     </td>
        #     <!--
        # 	    <td><div style="height: 10px">%</div><div class="segment segment-green" style="width:%;"></div></td><td><div style="height: 10px">%</div><div class="segment segment-green" style="width:%;"></div></td>
        # 	-->
        #     <td>
        #         <div style="height: 10px">
        #             51.53%
        #         </div>
        #         <div class="segment segment-gold" style="width:51.5318796577%;">
        #         </div>
        #     </td>
        #     <td>
        #         <div style="height: 10px">
        #             61591
        #         </div>
        #         <div class="segment segment-green" style="width:%;">
        #         </div>
        #     </td>
        # </tr>
        heroName = row.find("a")["href"].split('/')[3]
        matchUpAntiValue = float(row.findAll("td")[1].getText()[0:-1]) / 100
        matchUpAnti[heroName] = matchUpAntiValue
    for row in combData.findAll("tr"):
        heroName = row.find("a")["href"].split('/')[3]
        matchUpCombValue = float(row.findAll("td")[1].getText()[0:-1]) / 100
        matchUpComb[heroName] = matchUpCombValue
    return matchUpAnti, matchUpComb

def getUrlSoup(url):
    res = requests.get(url, headers={"User-Agent": "Mozilla/5.0"})
    return BeautifulSoup(res.content, 'html.parser')


def main():
    # get winrate and heroNames
    url = 'http://www.dotamax.com/hero/rate/'

    soup = getUrlSoup(url)
    data = soup.find("table", {"class": "sortable table-list"})
    heroNames = []
    winRates = {}
    matchUpAnti = {}
    matchUpComb = {}
    for row in data.findAll("tr"):
        heroName, winRate = getWinRate(row)
        heroNames.append(heroName)
        winRates[heroName] = winRate
        matchUpAnti[heroName], matchUpComb[heroName] = getAntiComb(heroName)
        print(heroName)
        print(winRates[heroName])
        print(matchUpAnti[heroName])
        print("----------------------")
        print(matchUpComb[heroName])
        print("----------------------")
    with open('winRate.txt', 'w') as file:
        file.write(json.dumps(winRates))
    with open('anti.txt', 'w') as file:
        file.write(json.dumps(matchUpAnti))
    with open('comb.txt', 'w') as file:
        file.write(json.dumps(matchUpComb))

if __name__ == "__main__":
    main()

