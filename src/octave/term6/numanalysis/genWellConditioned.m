function [ret] = genWellConditioned(n)
   ret = zeros(n, n);
   for i = 1 : n
       for j = 1 : n
            ret(i, j) = (-0.5) ^ abs(j - i) / n;
       end;
       ret(i, i) = -1;
   end;
end
