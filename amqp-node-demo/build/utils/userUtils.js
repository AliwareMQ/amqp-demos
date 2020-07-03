"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const js_base64_1 = require("js-base64");
const cryptoJS = require("crypto-js");
const ACCESS_FROM_USER = 0;
/**
 * 获取用户名
 * @param accessKeyId
 * @param instanceId
 */
function getUserName(accessKeyId, instanceId, securityToken) {
    if (securityToken) {
        return js_base64_1.Base64.encode([ACCESS_FROM_USER, instanceId, accessKeyId, securityToken].join(':'));
    }
    return js_base64_1.Base64.encode([ACCESS_FROM_USER, instanceId, accessKeyId].join(':'));
}
exports.getUserName = getUserName;
/**
 * 获取密码
 * @param accessKeySecret
 */
function getPassword(accessKeySecret) {
    const timestamp = Date.now().toString();
    const signature = cryptoJS.HmacSHA1(accessKeySecret, timestamp).toString().toUpperCase();
    return js_base64_1.Base64.encode([signature, timestamp].join(':'));
}
exports.getPassword = getPassword;
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoidXNlclV0aWxzLmpzIiwic291cmNlUm9vdCI6IiIsInNvdXJjZXMiOlsiLi4vLi4vc3JjL3V0aWxzL3VzZXJVdGlscy50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOztBQUFBLHlDQUFtQztBQUNuQyxzQ0FBc0M7QUFFdEMsTUFBTSxnQkFBZ0IsR0FBVyxDQUFDLENBQUM7QUFFbkM7Ozs7R0FJRztBQUNILFNBQWdCLFdBQVcsQ0FBQyxXQUFtQixFQUFFLFVBQWtCLEVBQUUsYUFBc0I7SUFDekYsSUFBSSxhQUFhLEVBQUU7UUFDakIsT0FBTyxrQkFBTSxDQUFDLE1BQU0sQ0FBQyxDQUFDLGdCQUFnQixFQUFFLFVBQVUsRUFBRSxXQUFXLEVBQUUsYUFBYSxDQUFDLENBQUMsSUFBSSxDQUFDLEdBQUcsQ0FBQyxDQUFDLENBQUM7S0FDNUY7SUFFRCxPQUFPLGtCQUFNLENBQUMsTUFBTSxDQUFDLENBQUMsZ0JBQWdCLEVBQUUsVUFBVSxFQUFFLFdBQVcsQ0FBQyxDQUFDLElBQUksQ0FBQyxHQUFHLENBQUMsQ0FBQyxDQUFDO0FBQzlFLENBQUM7QUFORCxrQ0FNQztBQUVEOzs7R0FHRztBQUNILFNBQWdCLFdBQVcsQ0FBQyxlQUF1QjtJQUNqRCxNQUFNLFNBQVMsR0FBVyxJQUFJLENBQUMsR0FBRyxFQUFFLENBQUMsUUFBUSxFQUFFLENBQUM7SUFDaEQsTUFBTSxTQUFTLEdBQVcsUUFBUSxDQUFDLFFBQVEsQ0FBQyxlQUFlLEVBQUUsU0FBUyxDQUFDLENBQUMsUUFBUSxFQUFFLENBQUMsV0FBVyxFQUFFLENBQUM7SUFFakcsT0FBTyxrQkFBTSxDQUFDLE1BQU0sQ0FBQyxDQUFDLFNBQVMsRUFBRSxTQUFTLENBQUMsQ0FBQyxJQUFJLENBQUMsR0FBRyxDQUFDLENBQUMsQ0FBQztBQUN6RCxDQUFDO0FBTEQsa0NBS0MifQ==