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
    const { accessKeyId, accessKeySecret, securityToken, resourceOwnerId, } = config;
    const username = userUtils.getUserName(accessKeyId, resourceOwnerId, securityToken);
    const password = userUtils.getPassord(accessKeySecret);
    return amqplib.connect(transformUrl(url), Object.assign({}, options, { credentials: credentials.plain(username, password) }));
}
module.exports = (config) => (amqplib) => (Object.assign({}, amqplib, { connect(url, options) {
        return connect({ amqplib, config, url, options });
    } }));
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiaW5kZXguanMiLCJzb3VyY2VSb290IjoiIiwic291cmNlcyI6WyIuLi9zcmMvaW5kZXgudHMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7QUFBQSwyQ0FBMkM7QUFDM0MsK0NBQStDO0FBRS9DOzs7R0FHRztBQUNILE1BQU0sWUFBWSxHQUF1QixHQUFHLENBQUMsRUFBRSxDQUM3QyxHQUFHLENBQUMsT0FBTyxDQUFDLFNBQVMsQ0FBQyxLQUFLLENBQUMsQ0FBQyxDQUFDLENBQUMsR0FBRyxDQUFDLENBQUMsQ0FBQyxVQUFVLEdBQUcsRUFBRSxDQUFDO0FBRXZEOzs7R0FHRztBQUNILFNBQVMsT0FBTyxDQUFDLEVBQUUsT0FBTyxFQUFFLE1BQU0sRUFBRSxHQUFHLEVBQUUsT0FBTyxFQUFFO0lBQ2hELE1BQU0sRUFBRSxXQUFXLEVBQUUsR0FBcUMsT0FBTyxDQUFDO0lBQ2xFLE1BQU0sRUFDSixXQUFXLEVBQ1gsZUFBZSxFQUNmLGFBQWEsRUFDYixlQUFlLEdBQ2hCLEdBS0csTUFBTSxDQUFDO0lBRVgsTUFBTSxRQUFRLEdBQVcsU0FBUyxDQUFDLFdBQVcsQ0FBQyxXQUFXLEVBQUUsZUFBZSxFQUFFLGFBQWEsQ0FBQyxDQUFDO0lBQzVGLE1BQU0sUUFBUSxHQUFXLFNBQVMsQ0FBQyxVQUFVLENBQUMsZUFBZSxDQUFDLENBQUM7SUFFL0QsT0FBTyxPQUFPLENBQUMsT0FBTyxDQUFDLFlBQVksQ0FBQyxHQUFHLENBQUMsb0JBQ25DLE9BQU8sSUFDVixXQUFXLEVBQUUsV0FBVyxDQUFDLEtBQUssQ0FBQyxRQUFRLEVBQUUsUUFBUSxDQUFDLElBQ2xELENBQUM7QUFDTCxDQUFDO0FBRUQsTUFBTSxDQUFDLE9BQU8sR0FBRyxDQUFDLE1BQTZCLEVBQUUsRUFBRSxDQUFDLENBQUMsT0FBb0IsRUFBRSxFQUFFLENBQUMsbUJBQ3pFLE9BQU8sSUFDVixPQUFPLENBQUMsR0FBVyxFQUFFLE9BQVk7UUFDL0IsT0FBTyxPQUFPLENBQUMsRUFBRSxPQUFPLEVBQUUsTUFBTSxFQUFFLEdBQUcsRUFBRSxPQUFPLEVBQUUsQ0FBQyxDQUFDO0lBQ3BELENBQUMsSUFDRCxDQUFDIn0=