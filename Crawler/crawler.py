import urllib.request
import queue
import heapq
import time
# Get hyperlinks
# from bs4 import BeautifulSoup
import collections
import numpy as np
import htmlParse

class crawler(object):

	def __init__(self,iniLinks,N):

		INITIAL_LINK = iniLinks
		self.MAX_VISITED_SIZE = 	N

		self.i = 0
		self.linkToId = {}
		self.idToLink = {}
		self.idToHTML = {}
		self.sourceId = 0

		self.linkGraph = {}
		self.outIdNum = collections.defaultdict(int)
		self.urlQueue = []
		self.visited = set([INITIAL_LINK])
		self.errorLink = set()
		self.usedIdSet = set()
		self.stack = []
		heapq.heappush(self.urlQueue, (-1.0/N,self.i))


	def pageRank(self,linkGraph,last):

		threshold = 10**(-5)
		n = last
		pre = np.array([1.0/n for _ in range(n)]).reshape(n,1)
		#print(pre)

		A = [[0]*n for _ in range(n)]
		A = np.matrix(A)
		for i in range(last):
			pro = 1.0/self.outIdNum[i] if self.outIdNum[i] else 0
			for j in range(last):
				try:
					if (j,i) in self.linkGraph:	A[j,i] = pro
				except IndexError:
					print("Error", j, i, n, last)
		alpha = 0.85
		T = np.array([0.15/n for _ in range(n)]).reshape(n,1)
		while True:
			nex = T + np.dot(alpha*A,pre)
			gap = np.sum(np.absolute(np.subtract(nex,pre)))
			#print(gap)
			if gap <= threshold:	
				#print (gap)
				break
			pre = nex
		return nex

	def checkLinkValid(self,link):
		try:
			response = urllib.request.urlopen(link)
			try:
				result = response.read()
				self.idToLink[self.i] = link
				self.linkToId[link] = self.i
				self.idToHTML[self.i] = str(result)	
				self.linkGraph[self.i,self.sourceId] = 1
				self.outIdNum[self.sourceId] += 1
				self.i += 1
				return True
				# return str(result)
			except Exception as e:
				print ("Occurred error " + str(e), self.i, self.sourceId)
				return False
		except urllib.error.HTTPError as e:
			print ("Occurred error " + str(e), link)
			return False
		except urllib.error.URLError as e:
			print ("Occurred error " + str(e))
			return False
		except ConnectionResetError as e:
			print ("Occurred error " + str(e))
			return False
		except Exception as e:
			print ("Occurred error " + str(e))
			return False

	def updateNextLinks(self, attrs):
		for name, value in attrs:
			if self.i < self.MAX_VISITED_SIZE:
            # 判断标签<a>的属性
				if name == 'href':
					if value and "/item/" == value[:6] and value not in self.visited and value not in self.errorLink:
						status = self.checkLinkValid("https://baike.baidu.com" + value)
						if status != False:	self.visited.add(value)	
						else:	self.errorLink.add(value)
			else:	
				if value in self.visited:
					idx = self.linkToId["https://baike.baidu.com" + value]
					self.linkGraph[idx,self.sourceId] = 1
					self.outIdNum[self.sourceId] += 1

		nex = self.pageRank(self.linkGraph,self.i)
		newUrlQueue = []
		for j,val in enumerate(nex):
			heapq.heappush(newUrlQueue,(-val[0], j))
		self.urlQueue = newUrlQueue

if __name__ == "__main__":
	link = "https://baike.baidu.com/item/china/165190"
	N = 30
	crawl = crawler(link,N)
	crawl.checkLinkValid(link)
	cnt = 1
	while cnt < N and crawl.urlQueue:
		print (cnt)
		# curPage = urlQueue.get()
		_,sourceId = heapq.heappop(crawl.urlQueue)
		while sourceId in crawl.usedIdSet and crawl.urlQueue:
			_,sourceId = heapq.heappop(crawl.urlQueue)

		if sourceId not in crawl.usedIdSet:
			crawl.stack.append(crawl.idToLink[sourceId])
			crawl.usedIdSet.add(sourceId)
			curPage = crawl.idToHTML[sourceId]
			crawl.sourceId = sourceId

			my = htmlParse.MyParser()
			my.feed(curPage)
			
			crawl.updateNextLinks(my.attrs)

			cnt += 1
	print(cnt, len(crawl.visited), crawl.i)
	for url in crawl.stack:
		print (url)