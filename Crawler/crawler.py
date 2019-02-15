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
		self.MAX_VISITED_SIZE = N
		# Used for recording link id
		self.i = 0
		# Map link to link id
		self.linkToId = {}
		# Map id to link
		self.idToLink = {}
		# map id to HTML contents
		self.idToHTML = {}
		# record the source id when find next level links
		self.sourceId = 0
		# Used for recording link relationship(out & in)
		self.linkGraph = {}
		# Count out links in one page
		self.outIdNum = collections.defaultdict(int)
		# Maintain a priority queue to get highest score in all the links
		self.urlQueue = []
		# Stored visited valid links
		self.visited = set([INITIAL_LINK])
		# Stored visited invalid links
		self.errorLink = set()
		# Stored poped id
		self.usedIdSet = set()
		# Stored the order of all links according to scores of each link
		self.stack = []
		# Set initial scores of first link
		heapq.heappush(self.urlQueue, (-1.0/N,self.i))

	# Used for calculate scores of all links according to current link relationship
	# Used iteration methods to get the convergence result
	def pageRank(self,linkGraph,last):

		threshold = 10**(-5)
		n = last
		pre = np.array([1.0/n for _ in range(n)]).reshape(n,1)

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
			if gap <= threshold:	
				break
			pre = nex
		return nex
	# Check the link found is valid or not
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
	# If link is valid then update the linkgraph and related information
	# Used pageRank algorithm get a new queue according to new graph relationships
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


