-- lua logic：
-- 1. judge if stock is existing firstly, if it is not existing, return -1
-- 2. judge increment quantity is less than 0 or equal 0, if not, return -2
-- 3. if increase stock success, return 1
-- two params：
-- KEYS[1] : stock key
-- ARGV[1] : increase quantity
local stock = redis.call('get', KEYS[1])

-- stock exist
if not stock then
    return -1
end

local paramStock = tonumber(ARGV[1])

-- quantity is less than 0 or equal 0
if paramStock <= 0 then
    return -2
end

redis.call('incrby',KEYS[1], paramStock)
return 1