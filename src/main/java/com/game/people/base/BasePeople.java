package com.game.people.base;

import com.game.IBase.IFishing;
import com.game.IBase.IScan;
import com.game.IBase.IWalk;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * <pre>
 * 人物基类
 * </pre>
 *
 * @author yuxuan
 * @time 2020-11-23 14:33
 */
public abstract class BasePeople implements IFishing, IWalk, IScan {
    private AtomicInteger state;
}
