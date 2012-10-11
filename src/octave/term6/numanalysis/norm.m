function [ret] = norm(A)
    ret = sum(sum(A .^ 2));
end
