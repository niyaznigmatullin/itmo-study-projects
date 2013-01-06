import random

#len = random.randint(4, 1000000)
len = 10**6
s = ""
for i in range(len):
    s += chr(ord('a') + random.randrange(4))
print(s)
