function [ret] = genIllConditioned(n)
    ret = zeros(n, n);
    for i = 1 : n
        for j = 1 : n
            ret(i, j) = 1 / (i + j + 1);
        end;
    end;
end;
