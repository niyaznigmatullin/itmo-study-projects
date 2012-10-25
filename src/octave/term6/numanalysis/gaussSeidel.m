function [x, xs, norms] = gaussSeidel(A, b, eps, maxIterations)
    n = size(A, 1);
    x = zeros(n, 1);
    z = diag(A);
    A = A - (eye(n) .* A);
    A = [-A b];
    A = A ./ (z * ones(1, n + 1));
    good = 1;
    norms = [];
    x = [x; 1];
    xs = [x(1:n)];
    while (good && size(xs) < maxIterations)
        old = x;
        for i = 1 : n
            x(i) = 0;
            x(i) = A(i, :) * x;
        end;
        xs = [xs;x(1:n)];
        norms = [norms; norm(x - old)];
        good = norms(end) >= eps;
    end;
    norms = [norms; norms(end)]; 
    x = xs(end);
end
