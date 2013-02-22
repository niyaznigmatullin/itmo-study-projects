from fractions import Fraction
import math

line = input()

g = 1
freq = {}
s = ""
for i in range(len(line)):
    c = line[i]
    if c in freq:   
        p = Fraction(freq[c], i + 1)
        freq[c] += 1        
    else:        
        p = Fraction(1, (i + 1) * (256 - len(freq)))
        freq[c] = 1
    g = g * p
    s += " \\hline "
    s += " " + ("\\_" if c == '_' else str(c)) + "&" + str(p) + "&" + str(1. * g.numerator / g.denominator) + "\\\\"
s += " \\hline "
   
gg = 1
z = 0
while gg >= g:
    gg = gg * Fraction(1, 2)
    z += 1

print(s)
print(z)
print(g)
