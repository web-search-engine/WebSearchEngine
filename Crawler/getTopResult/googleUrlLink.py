import urllib
import json
import numpy as np
print('Gooling......')

def search(search_string):
    query = urllib.parse.urlencode({'q': search_string})
    cx = urllib.parse.urlencode({'cx': '016370974429777403833:vyjlpbhxzx8'})
    num = urllib.parse.urlencode({'num': '30'})
    key = urllib.parse.urlencode({'key': 'AIzaSyCP6K6SNhPmQKuhRTdkrWJ1Q0gReXLo4kM'})
    url = 'https://www.googleapis.com/customsearch/v1?'+ query +'&'+ cx+'&' + num +'&' + key
        
    results = json.loads(search_results)
    data = results['items']
    
    links = []
    i = 0
    cnt = 0
    while i < len(data) and cnt < 10:
        try:
            search_response = urllib.request.urlopen(data[i]['link'])
            try:
                search_results = search_response.read().decode("utf8")
            except Exception as e:
                print ("error occurred" + str(e))
                i += 1
                continue
        except Exception as e:
            print("error occurred" + str(e))
            i += 1
            continue
        links.append(data[i]['link'])
        cnt += 1
    return links

