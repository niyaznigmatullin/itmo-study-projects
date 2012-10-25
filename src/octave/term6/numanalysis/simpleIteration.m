function [x, xs, norms] = simpleIteration(A, b, eps, maxIterations)
    n = size(A);
    x = zeros(n, 1);
    xs = [x];
    norms = [];
    good = 1;
    while (good && size(xs) < maxIterations)
        next = (A * x) - b + x;
        norms = [norms; norm(x - next)];
        xs = [xs; next];
        good = norm(x - next) >= eps;
        x = next;
    end;
    norms = [norms; norms(end)];
    x = xs(end);
end;
