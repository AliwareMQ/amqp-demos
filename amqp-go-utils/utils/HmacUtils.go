package utils

import (
	"crypto/hmac"
	"crypto/sha1"
	"encoding/hex"
)

func HmacSha1(keyStr string, message string) string {
	mac := hmac.New(sha1.New, []byte(message))
	mac.Write([]byte([]byte(keyStr)))
	return hex.EncodeToString(mac.Sum(nil))
}
