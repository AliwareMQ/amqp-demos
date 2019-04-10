/// <reference path="typings/index.d.ts" />
import * as userUtils from './utils/userUtils';

/**
 * 判断url是否已添加amqp协议
 * @param url
 */
const transformUrl: (string) => string = url =>
  url.indexOf('amqp://') === 0 ? url : `amqp://${url}`;

/**
 * 生成阿里云账号的密码 再链接
 * @param param0
 */
function connect({ amqplib, config, url, options }): Promise<any> {
  const { credentials }: { credentials: credentialsType } = amqplib;
  const {
    accessKeyId,
    accessKeySecret,
    securityToken,
    resourceOwnerId,
  }: {
    accessKeyId: string;
    accessKeySecret: string;
    securityToken?: string;
    resourceOwnerId: number;
  } = config;

  const username: string = userUtils.getUserName(accessKeyId, resourceOwnerId, securityToken);
  const password: string = userUtils.getPassord(accessKeySecret);

  return amqplib.connect(transformUrl(url), {
    ...options,
    credentials: credentials.plain(username, password),
  });
}

module.exports = (config: AliyunCredentialsType) => (amqplib: amqplibType) => ({
  ...amqplib,
  connect(url: string, options: any) {
    return connect({ amqplib, config, url, options });
  },
});
