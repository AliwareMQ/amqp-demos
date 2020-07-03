"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
/// <reference path="typings/index.d.ts" />
const userUtils = require("./utils/userUtils");
/**
 * 判断url是否已添加amqp协议
 * @param url
 */
const transformUrl = url => url.indexOf('amqp://') === 0 ? url : `amqp://${url}`;
/**
 * 生成阿里云账号的密码 再链接
 * @param param0
 */
function connect({ amqplib, config, url, options }) {
    const { credentials } = amqplib;
    const { accessKeyId, accessKeySecret, securityToken, instanceId, } = config;
    const username = userUtils.getUserName(accessKeyId, instanceId, securityToken);
    const password = userUtils.getPassword(accessKeySecret);
    return amqplib.connect(transformUrl(url), Object.assign(Object.assign({}, options), { credentials: credentials.plain(username, password) }));
}
module.exports = (config) => (amqplib) => (Object.assign(Object.assign({}, amqplib), { connect(url, options) {
        return connect({ amqplib, config, url, options });
    } }));
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiaW5kZXguanMiLCJzb3VyY2VSb290IjoiIiwic291cmNlcyI6WyIuLi9zcmMvaW5kZXgudHMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7QUFBQSwyQ0FBMkM7QUFDM0MsK0NBQStDO0FBRS9DOzs7R0FHRztBQUNILE1BQU0sWUFBWSxHQUF1QixHQUFHLENBQUMsRUFBRSxDQUM3QyxHQUFHLENBQUMsT0FBTyxDQUFDLFNBQVMsQ0FBQyxLQUFLLENBQUMsQ0FBQyxDQUFDLENBQUMsR0FBRyxDQUFDLENBQUMsQ0FBQyxVQUFVLEdBQUcsRUFBRSxDQUFDO0FBRXZEOzs7R0FHRztBQUNILFNBQVMsT0FBTyxDQUFDLEVBQUUsT0FBTyxFQUFFLE1BQU0sRUFBRSxHQUFHLEVBQUUsT0FBTyxFQUFFO0lBQ2hELE1BQU0sRUFBRSxXQUFXLEVBQUUsR0FBcUMsT0FBTyxDQUFDO0lBQ2xFLE1BQU0sRUFDSixXQUFXLEVBQ1gsZUFBZSxFQUNmLGFBQWEsRUFDYixVQUFVLEdBQ1gsR0FLRyxNQUFNLENBQUM7SUFFWCxNQUFNLFFBQVEsR0FBVyxTQUFTLENBQUMsV0FBVyxDQUFDLFdBQVcsRUFBRSxVQUFVLEVBQUUsYUFBYSxDQUFDLENBQUM7SUFDdkYsTUFBTSxRQUFRLEdBQVcsU0FBUyxDQUFDLFdBQVcsQ0FBQyxlQUFlLENBQUMsQ0FBQztJQUVoRSxPQUFPLE9BQU8sQ0FBQyxPQUFPLENBQUMsWUFBWSxDQUFDLEdBQUcsQ0FBQyxrQ0FDbkMsT0FBTyxLQUNWLFdBQVcsRUFBRSxXQUFXLENBQUMsS0FBSyxDQUFDLFFBQVEsRUFBRSxRQUFRLENBQUMsSUFDbEQsQ0FBQztBQUNMLENBQUM7QUFFRCxNQUFNLENBQUMsT0FBTyxHQUFHLENBQUMsTUFBNkIsRUFBRSxFQUFFLENBQUMsQ0FBQyxPQUFvQixFQUFFLEVBQUUsQ0FBQyxpQ0FDekUsT0FBTyxLQUNWLE9BQU8sQ0FBQyxHQUFXLEVBQUUsT0FBWTtRQUMvQixPQUFPLE9BQU8sQ0FBQyxFQUFFLE9BQU8sRUFBRSxNQUFNLEVBQUUsR0FBRyxFQUFFLE9BQU8sRUFBRSxDQUFDLENBQUM7SUFDcEQsQ0FBQyxJQUNELENBQUMifQ==