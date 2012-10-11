function [x] = simpleIteration(A, b, eps)
    n = size(A);
    x = zeros(n, 1);
    good = 1;
    countIterations = 0;
    while (good)
        countIterations = countIterations + 1;
        next = (A * x) - b + x;
        good = norm(x - next) >= eps;
        x = next;
    end;
    fprintf('converged in %d iterations\n', countIterations);
end;
