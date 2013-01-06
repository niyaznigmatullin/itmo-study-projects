#include <cstdio>

const int MAXN = 5555555;

int s[MAXN], sa[MAXN], sa2[MAXN], h[MAXN], h2[MAXN], cnt[MAXN], c[MAXN], c2[MAXN];
int n;

int good(int z) {
    if (z >= n) z -= n;
    if (z < 0) z += n;
    return z;
}

void buildSA() {
    int maxa = 0;
    for (int i = 0; i < n; i++) {
        if (maxa < s[i]) maxa = s[i];
        cnt[s[i]]++;
        c[i] = s[i];
    }
    int cur = 0;
    for (int i = 0; i <= maxa; i++) {
        h2[i] = h[i] = cur;
        cur += cnt[i];
    }
    for (int i = 0; i < n; i++) {
        sa[h2[s[i]]++] = i;
    }
    for (int d = 1; d < n; d <<= 1) {
        for (int i = 0; i < n; i++) {
            int j = good(sa[i] - d);
            sa2[h[c[j]]++] = j;
        }
        c2[sa2[0]] = 0;
        h[0] = 0;
        int classes = 1;
        for (int i = 1; i < n; i++) {
            if (c[sa2[i]] != c[sa2[i - 1]] || c[good(sa2[i] + d)] != c[good(sa2[i - 1] + d)]) {
                h[classes++] = i;
            }
            c2[sa2[i]] = classes - 1;
        }
        for (int i = 0; i < n; i++) {
            c[i] = c2[i];
            sa[i] = sa2[i];
        }
    } 
}

int main() {
    int c;
    do {
        c = getchar();
    } while (c < 'a' || c > 'z');
    n = 0;
    while (c >= 'a' && c <= 'z') {
        s[n++] = c - 'a' + 1;
        c = getchar();
    }
    s[n++] = 0;
    buildSA();
    for (int i = 0; i < n; i++) {
        printf("%d%c", sa[i], " \n"[i == n - 1]);
    }
}

