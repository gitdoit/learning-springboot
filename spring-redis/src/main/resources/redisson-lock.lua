local key = KEYS[1]; --锁的key
local threadId = ARGV[1];--线程唯一标识
local releaseTime = ARGV[2];--锁的自动释放时间
if (redis.call('HEXISTS',key,threadId) == 0) then
    return nil;
end ;
local count = redis.call('HINCRBY',key,threadId,-1);
if(count > 0) then
    redis.call('EXPIRE',key,releaseTime);
    return nil;
else
    redis.call('DEL',key)
    return nil;
end