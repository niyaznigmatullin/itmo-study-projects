function [x] = gaussSeidel(A, b, eps)
    n = size(A, 1);
    x = zeros(n, 1);
    z = diag(A);
    A = A - (eye(n) .* A);
    A = [-A b];
    A = A ./ (z * ones(1, n + 1));
    good = 1;

    x = [x; 1];
    countIterations = 0;
    while (good)
        countIterations = countIterations + 1;
        old = x;
        for i = 1 : n
            x(i) = 0;
            x(i) = A(i, :) * x;
        end;
        norm(x - old)
        good = norm(x - old) >= eps;
    end;
    x = x(1:n);
    fprintf('Gauss-Seidel converged in %d step(s)\n', countIterations);
end
