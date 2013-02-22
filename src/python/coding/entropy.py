import sys
import math

def hash(a):
    h = 0
    for i in a:
        h = h * 256 + i
    return h

f = open(sys.argv[1], "rb")
s = f.read()

#s = ""
N = len(s)
for n in range(1, 5):
    a = {}
    cnt = N - n + 1
    for i in range(0, cnt):
        z = hash(s[i:i+n])
        if z in a:
            a[z] += 1
        else:
            a[z] = 1
    cntbad = 256**n - len(a)
    p0 = (1 - 1. * cntbad / (N**n))
    h = 0
    h2 = -math.log(1. / (N**n)) * cntbad / (N**n)
    for i in a.values():
        p = 1. * i / cnt
        p2 = p * p0
        h -= math.log(p) * p
        if cntbad != N**n:
            h2 -= math.log(p2) * p2
    h /= math.log(2)
    h2 /= math.log(2)
    h /= n
    h2 /= n
    print(h, h2)
