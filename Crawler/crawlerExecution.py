
def crawlerExecution (crawl): 
	cnt = 1
	while cnt < N and crawl.urlQueue:
		print (cnt)
		_,sourceId = heapq.heappop(crawl.urlQueue)
		# check the sourceId is used or not
		# if used then continue pop until the first one that not used
		while sourceId in crawl.usedIdSet and crawl.urlQueue:
			_,sourceId = heapq.heappop(crawl.urlQueue)
		# if the queue is empty and jump out of loops
		if sourceId not in crawl.usedIdSet:
			crawl.stack.append(crawl.idToLink[sourceId])
			crawl.usedIdSet.add(sourceId)
			curPage = crawl.idToHTML[sourceId]
			crawl.sourceId = sourceId
			# starting html parsing
			my = htmlParse.MyParser()
			my.feed(curPage)
			
			crawl.updateNextLinks(my.attrs)

			cnt += 1
	print(cnt, len(crawl.visited), crawl.i)
	for url in crawl.stack:
		print (url)