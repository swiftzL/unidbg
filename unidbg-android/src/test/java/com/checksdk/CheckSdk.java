package com.checksdk;

import com.github.unidbg.AndroidEmulator;
import com.github.unidbg.arm.backend.Unicorn2Factory;
import com.github.unidbg.linux.android.AndroidEmulatorBuilder;
import com.github.unidbg.linux.android.AndroidResolver;
import com.github.unidbg.linux.android.dvm.*;
import com.github.unidbg.memory.Memory;
import sun.security.krb5.internal.crypto.Des;

import javax.crypto.spec.DESedeKeySpec;
import java.io.File;


class s {

}

public class CheckSdk extends AbstractJni {
    @Override
    public DvmObject<?> getStaticObjectField(BaseVM vm, DvmClass dvmClass, String signature) {
        if (signature.equals("android/os/Build->FINGERPRINT:Ljava/lang/String;")) {
            return new StringObject(vm,"aosp");
        }
        return super.getStaticObjectField(vm, dvmClass, signature);
    }

    private final AndroidEmulator emulator;
    private final VM vm;

    public CheckSdk() {
        emulator = AndroidEmulatorBuilder
                .for32Bit()
                .addBackendFactory(new Unicorn2Factory(true))
                .setProcessName("com.roysue.easyso1")
                .build();
        Memory memory = emulator.getMemory();
        memory.setLibraryResolver(new AndroidResolver(23));
        vm = emulator.createDalvikVM(new File("unidbg-android/src/test/resources/check/app-debug.apk"));
        vm.setJni(this);
        vm.setVerbose(true);
        DalvikModule dm = vm.loadLibrary("roysue", true);
        dm.callJNI_OnLoad(emulator);
        DvmClass dvmClass = vm.resolveClass("com/roysue/easyso1/MainActivity");
        DvmObject<?> requestuserinfo = dvmClass.callStaticJniMethodObject(emulator, "Sign(Ljava/lang/String;)Ljava/lang/String;", "requestuserinfo");
        System.out.println(requestuserinfo);


    }

    public static void main(String[] args) {
        CheckSdk cs = new CheckSdk();

    }

}
