function [x, xs, norms] = sor(A, b, w, eps, maxIterations)
    n = size(A, 1);
    x = zeros(n, 1);
    xs = [x];
    norms = [];

    z = diag(A);
    A = A - (eye(n) .* A);
    A = [-A b];
    A = A ./ (z * ones(1, n + 1));

    good = 1;

    x = [x; 1];
    norms = [];
    while (good && size(xs) < maxIterations)
        old = x;
        xn = [A * x; 1];
        for i = 1 : n
            x(i) = (1 - w) * x(i) + w * (A(i, :) * x);
        end;
        norms = [norms; norm(x - old)];
        xs = [xs; x];
        good = norm(x - old) >= eps;
    end;
    norms = [norms; norms(end)];
    x = xs(end);
end
