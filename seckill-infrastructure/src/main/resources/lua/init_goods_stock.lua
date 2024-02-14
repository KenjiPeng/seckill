-- lua logic：
-- 1. judge pass param is less than 0 or equal 0, if not, return -2
-- 2. if init stock success, return 1
-- two params：
-- KEYS[1] : stock key
-- ARGV[1] : stock init quantity
local paramStock = tonumber(ARGV[1])

-- param must not be less than 0 or equal 0
if paramStock <= 0 then
    return -2
end

redis.call('set',KEYS[1], paramStock)
return 1