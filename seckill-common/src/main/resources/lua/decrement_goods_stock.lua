-- lua logic：
-- 1. judge if stock is existing firstly, if it is not existing, return -1
-- 2. judge decrement quantity is more than 0, if not, return -2
-- 3. if it is not enough stock return -3; if decrease stock success, return 1
-- two params：
-- KEYS[1] : activity stock
-- ARGV[1] : decrease quantity
local stock = redis.call('get', KEYS[1])

-- stock exist
if not stock then
    return -1
end

-- Get stock from redis and decrease quantity
local intStock = tonumber(stock)
local paramStock = tonumber(ARGV[1])

-- quantity is more than 0
if paramStock <= 0 then
    return -2
end

-- not enough stock
if intStock < paramStock then
    return -3
end

-- decrease stock
redis.call('decrby',KEYS[1], paramStock)

return 1