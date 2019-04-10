declare interface AliyunCredentialsType {
    /**
     * Access Key ID.
     */
    accessKeyId: string;
    /**
     * Access Key Secret.
     */
    accessKeySecret: string;
    /**
     * security temp token. (optional)
     */
    securityToken?: string;
    /**
     * 资源owner账号（主账号）
     */
    resourceOwnerId: number;
}

declare interface credentialsType {
    plain: (user: string, passwd: string) => Object;
    [other: any]: any
}

declare interface amqplibType {
    connect: (url: string, connOptions: any) => Promise<any>;
    credentials: credentialsType;
    IllegalOperationError: (msg: string, stack) => void;
}
