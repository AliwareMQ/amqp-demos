package utils

import (
	"crypto/hmac"
	"crypto/sha1"
	"encoding/hex"
)

func HmacSha1(keyStr string, message string) string {
	key := []byte(keyStr)
	mac := hmac.New(sha1.New, key)
	mac.Write([]byte(message))
	return hex.EncodeToString(mac.Sum(nil))
}
