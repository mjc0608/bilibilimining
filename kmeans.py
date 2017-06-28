# encoding=utf-8

import os
import sys
import math
import copy
import jieba
import gensim
import random
import logging
import numpy as np
from gensim.models import Word2Vec

reload(sys)
sys.setdefaultencoding( "utf-8" )

basePath = '/home/howie/DataMining/txt'
dictPath = '/home/howie/DataMining/dict.txt'
resultPath = '/home/howie/DataMining/res/'
modelPath = 'word2vector.model'
k = 4

class TextLoader(object):
    def __init__(self):
        pass

    def __iter__(self):
        input = open(dictPath,'r')
        line = str(input.readline())
        counter = 0
        i = 0
        while line:
            segments = line.split(' ')
            segments.pop()
            if len(segments) > 0:
            	yield  segments
            line = str(input.readline())
        input.close()

def cut():
	files = os.listdir(basePath)
	output = open(dictPath, 'w')
	for file in files:
		f = open(basePath + "/" + file)
		lines = iter(f)
		for line in lines:
			seg_list = jieba.cut(line)
			output.write(" ".join(seg_list).encode('utf-8'))
		f.close()
	output.close()

def generateModel():
	sentences = TextLoader()
	model = gensim.models.Word2Vec(sentences, workers=8, min_count=1)
	model.save(modelPath)
	print('word2vector model has been successfully generated')

def sentence2vector():
	model = Word2Vec.load('word2vector.model')
	sentences = []
	dict = open(dictPath, "r")
	line = str(dict.readline())
	while line:
	    segments = line.split(' ')
	    segments.pop()
	    if len(segments) > 0:
	    	sentence = model[segments[0]]
	    	for word in segments:
	    		sentence = sentence + model[word]
	    	sentence = sentence - model[segments[0]]
	    	sentence = sentence / len(segments)
	    sentences.append(sentence)
	    line = str(dict.readline())
	dict.close()
	print('sentence2vector successfully finished')
	print(str(len(sentences)) + ' sentences in total')
	return sentences

def dist(a, b):
	r = 0
	for i in range(0, a.size):
		r += math.pow((a[i]-b[i]), 2)
	return r

def kmeans():
	sentences = sentence2vector()
	# initial k center points
	center = []
	identical = []
	while (len(center) < k):
		c = random.randint(0, len(sentences) - 1)
		if c not in identical:
			identical.append(c)
			center.append(copy.deepcopy(sentences[c]))
	mark = []
	for i in range(0, len(sentences)):
		mark.append(0)
	# main loop
	print('starting k-means...')
	iter = 0
	while True :
		iter += 1
		print('iterator ' + str(iter))
		finish = True
		for i in range(0, len(sentences)):
			r = 0
			min = dist(sentences[i], center[0])
			for j in range(1, k):
				now = dist(sentences[i], center[j])
				if now < min:
					r = j
					min = now
			if r != mark[i]:
				finish = False
				mark[i] = r
		if finish :
			break
		count = []
		for i in range(0, k):
			center[i] -= center[i]
			count.append(0)
		for i in range(0, len(sentences)):
			center[mark[i]] += sentences[i]
			count[mark[i]] += 1
		for i in range(0, k):
			center[i] /= count[i]
	print('kmeans finished')
	return mark

def classify():
	cnt = 0
	mark = kmeans()
	files= os.listdir(basePath)
	category = [None for i in range(k)]
	for i in range(0, k):
		category[i] = open(resultPath + str(i) + ".txt", 'w')
	for file in files:
		# corresponding result file
		newfile = resultPath + file
		output = open(newfile, 'w')
		f = open(basePath + "/" + file)
		lines = iter(f)
		for line in lines:
			output.write(str(mark[cnt]) + '\n')
			category[mark[cnt]].write(line)
			cnt += 1
		f.close()
		output.close()
	for i in range(0, k):
		category[i].close()
	print('results are stored in /res')

cut()
generateModel()
classify()