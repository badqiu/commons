log4j扩展:
1. SMTP邮件标题增加显示IP
2. 发送Sms短消息,带IP
3. 发送YYGroup消息,带IP
4. 通过环境变量  DWENV=prod来激活只在生产环境发送报警
5. 通过时间段控制，20分钟只发送一条消息
6. 通过cron来控制来实现: 9-18点1个小时发送一消息，18-8点2小时发一条消息


