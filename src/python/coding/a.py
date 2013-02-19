import sys
import math

f = sys.stdin
s = f.read()
N = len(s)
for n in range(1, 5):
    a = {}
    cnt = N - n + 1
    for i in range(0, cnt):
        z = s[i:i+n]
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
        h2 -= math.log(p2) * p2
    print(h, h2)
