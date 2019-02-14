from urllib import request
import simplejson
import urllib 
import json
#定义存储匹配路径的列表
linkElems = []
print('Gooling......')
 
#输入查找的关键词
keyWord = input('Enter KEYWORD:')
#拼接下载网页的路径
def search(search_string):
  query = urllib.parse.urlencode({'q': search_string})
  url = 'http://ajax.googleapis.com/ajax/services/search/web?v=1.0&%s' % query
  search_response = urllib.request.urlopen(url)
  search_results = search_response.read().decode("utf8")
  results = json.loads(search_results)
  print (results)
  data = results['responseData']
  print('Total results: %s' % data['cursor']['estimatedResultCount'])
  hits = data['results']
  print('Top %d hits:' % len(hits))
  for h in hits: print(' ', h['url'])
  print('For more results, see %s' % data['cursor']['moreResultsUrl'])
  return hits
print (search(str(keyWord)))

