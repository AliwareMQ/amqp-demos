import { Base64 } from 'js-base64';
import * as cryptoJS from 'crypto-js';

const ACCESS_FROM_USER: number = 0;

/**
 * 获取用户名
 * @param accessKeyId 
 * @param resourceOwnerId 
 */
export function getUserName(accessKeyId: string, resourceOwnerId: number, securityToken?: string): string {
  if (securityToken) {
    return Base64.encode([ACCESS_FROM_USER, resourceOwnerId, accessKeyId, securityToken].join(':'));
  }

  return Base64.encode([ACCESS_FROM_USER, resourceOwnerId, accessKeyId].join(':'));
}

/**
 * 获取密码
 * @param accessKeySecret 
 */
export function getPassord(accessKeySecret: string): string {
  const timestamp: string = Date.now().toString();
  const signature: string = cryptoJS.HmacSHA1(accessKeySecret, timestamp).toString().toUpperCase();
  
  return Base64.encode([signature, timestamp].join(':'));
}
