import os
from snownlp import SnowNLP

basePath = '/home/howie/DataMining/txt'
segPath = '/home/howie/DataMining/seg/'

files = os.listdir(basePath)
total = 652
cnt = 0
for file in files:
    cnt += 1
    print str(cnt) + '/' + str(total)
    f = open(basePath + "/" + file)
    output = open(segPath + file, 'w')
    lines = iter(f)
    for line in lines:
        s = SnowNLP(unicode(line, 'utf-8'))
        output.write(str(s.sentiments) + '\n')
    f.close()
    output.close()
