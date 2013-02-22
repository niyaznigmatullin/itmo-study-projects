
import sys
from collections import defaultdict
from fractions import Fraction
from decimal import Decimal
from functools import reduce
from math import ceil, log

def escape(s):
    ret = ""
    for c in s:
        if c == '_':
            ret += "\\_"
        else:
            ret += c
    return ret

def ppma(s, D=5):
    letters_left = 256
    frmt = '{}&{}&{}&{}&{}\\\\ '
    myoutput = ""

    def find_context(i):
        k = 0
        for j in range(min(i,D)+1):
            f = s.find(s[i-j:i], 0, i-1)
            if f == -1:
                break
            k = j
        return s[i-k:i]
    def context_stat(i, cont):
        stat = defaultdict(lambda: 0)
        p = 0
        while p < i:
            p = s.find(cont, p, i - 1)
            if p == -1:
                break
            stat[s[p+len(cont)]] += 1
            p += 1
        return stat
    def calc_numbers(i, context, seen=set()):
        c = s[i]
        stat = context_stat(i, context)
        for b in seen:
            del stat[b]
        sm = sum(stat.values())
        if c in stat.keys():
            return ([sm], [], Fraction(stat[c], sm+1))
        else: # need esc
            if context:
                taus, p_escs, p_a = calc_numbers(i, context[1:], set(stat.keys()) | seen)
            else:
                nonlocal letters_left
                taus, p_escs, p_a = [], [], Fraction(1, letters_left)
                letters_left -= 1
            return ([sm] + taus, [Fraction(1, sm + 1)] + p_escs, p_a)

    def fracs(fs):
        return ' * '.join(map(frac, fs))
    def frac(f):
        if f.denominator == 1:
            return str(f.numerator)
        else:
            return '{} / {}'.format(f.numerator, f.denominator)

    i = 0
    total = Decimal(0)
    while i < len(s):
        letter = s[i]
        context = find_context(i)
        taus, p_escs, p_a = calc_numbers(i, context)
        if not context:
            context = '-'
        elif isinstance(context, bytes):
            context = ' '.join(map(hex, context))
        fr1 = fracs(p_escs)
        #if fr1:
        #    fr1 = '$'+fr1+'$'
        fr2 = frac(p_a)
        #if fr2:
        #    fr2 = '$'+fr2+'$'
        myoutput += " \\hline "
        myoutput += frmt.format(letter,
                          context,
                          ';'.join(map(str,taus)),
                          fr1,
                          fr2)
        i += 1
        total += sum(map(lambda p: Decimal.from_float(-log(p, 2)), p_escs + [p_a]))
    myoutput += " \\hline "
    myoutput += '\nTotal bits: {}'.format(ceil(total)+1)
    return myoutput

line = input()
print(escape(ppma(line)))