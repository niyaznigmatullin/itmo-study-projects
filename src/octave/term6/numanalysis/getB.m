function [b] = getB(n)
    rand("seed", 100);
    b = rand(n, 1);
end;
