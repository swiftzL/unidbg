package com.xvideo;

import com.github.unidbg.AndroidEmulator;
import com.github.unidbg.arm.backend.Unicorn2Factory;
import com.github.unidbg.linux.android.AndroidEmulatorBuilder;
import com.github.unidbg.linux.android.AndroidResolver;
import com.github.unidbg.linux.android.dvm.AbstractJni;
import com.github.unidbg.linux.android.dvm.DalvikModule;
import com.github.unidbg.linux.android.dvm.DvmClass;
import com.github.unidbg.linux.android.dvm.VM;
import com.github.unidbg.memory.Memory;

import java.io.File;

public class Xvideo extends AbstractJni {

    private final AndroidEmulator emulator;
    private final DvmClass NativeApi;
    private final VM vm;

    public Xvideo() {
        emulator = AndroidEmulatorBuilder
                .for64Bit()
                .addBackendFactory(new Unicorn2Factory(true))
                .setProcessName("com.sina.oasis")
                .build();
        Memory memory = emulator.getMemory();
        memory.setLibraryResolver(new AndroidResolver(23));
        vm = emulator.createDalvikVM(new File("unidbg-android/src/test/resources/bilibili/lvzhou.apk"));
        vm.setJni(this);
        vm.setVerbose(true);
        DalvikModule dm = vm.loadLibrary("oasiscore", true);
        NativeApi = vm.resolveClass("com/weibo/xvideo/NativeApi");
        dm.callJNI_OnLoad(emulator);
    }

    public static void main(String[] args) {
        Xvideo xvideo = new Xvideo();
    }

}
