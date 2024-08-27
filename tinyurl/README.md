# TinyUrl Backend Code

Springboot starter project with 2 api
    POST : api/shorten - give original URL as string in body; this will create hash and store it in redis as key value pair
    GET: api/{shortUrl} - retrieves the original URL

# Hash function
   In Java, MessageDigest.getInstance("SHA-256") is used to create a MessageDigest object that implements the SHA-256 hash algorithm. SHA-256(Secure Hash Algorithm 256-bit) is a cryptographic hash function that generates a 256-bit (32-byte) hash value from the input data. This function is commonly used for data integrity verification and password hashing.
