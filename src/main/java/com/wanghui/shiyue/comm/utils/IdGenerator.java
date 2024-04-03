package com.wanghui.shiyue.comm.utils;

import com.github.yitter.contract.IdGeneratorOptions;
import com.github.yitter.idgen.YitIdHelper;

/**
 * @description: 雪花算法id生成
 * @fileName: IdGenerator
 * @author: wanghui
 * @createAt: 2024/02/19 11:30:45
 * @updateBy:
 * @copyright: 众阳健康
 */
public class IdGenerator {
    static Short workerId = 1;
    private static IdGeneratorOptions options = new IdGeneratorOptions(workerId);
    public static Long generator(){
        YitIdHelper.setIdGenerator(options);
        return YitIdHelper.nextId();
    }
}
