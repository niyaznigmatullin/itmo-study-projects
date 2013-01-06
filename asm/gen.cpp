#include <stdio.h>
#include <stdlib.h>

int main() { 
    int n = 99999999 + 2;
    printf("%d\n", n);
    for (int i = 0; i < n; i++) {
        int x = rand();
        if (x < 0) x = -x;
        printf("%d\n ", x);
    }
    return 0;
}
