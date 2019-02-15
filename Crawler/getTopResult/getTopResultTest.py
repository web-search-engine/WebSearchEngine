import urllib
import json
import numpy as np
print('Gooling......')

def search(search_string):
    query = urllib.parse.urlencode({'q': search_string})
    cx = urllib.parse.urlencode({'cx': '016370974429777403833:vyjlpbhxzx8'})
    num = urllib.parse.urlencode({'num': '10'})
    key = urllib.parse.urlencode({'key': 'AIzaSyCP6K6SNhPmQKuhRTdkrWJ1Q0gReXLo4kM'})
    url = 'https://www.googleapis.com/customsearch/v1?'+ query +'&'+ cx+'&' + num +'&' + key
    try:
        search_response = urllib.request.urlopen(url)
        try:
            search_results = search_response.read().decode("utf8")
        except Exception as e:
            print ("error occurred" + str(e))
    except Exception as e:
        print("error occurred" + str(e))
        
    results = json.loads(search_results)
    data = results['items']
    
    links = []
    for i in range (10):
        links.append(data[i]['link'])
    return links

