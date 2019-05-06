package com.hg.sj_app.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.hg.sj_app.module.EncryptionData;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class EncryptionFile {
    private Context context;
    private List<String> user;
    private ProcessListner processListner;
    private SharedPreferences preference;
    private EncryptionData encryptionData = new EncryptionData();


    public EncryptionFile(Context context) {
        this.context = context;
        preference = context.getSharedPreferences("user", MODE_PRIVATE);
    }

    public EncryptionFile(Context context, String path, List<String> user) {
        this.context = context;
        encryptionData.setPath(path);
        this.user = user;
        if (user.size() > 0) {
            encryptionData.setEncryption(true);
        }
        preference = context.getSharedPreferences("user", MODE_PRIVATE);
    }

    public void setPath(String path){
        encryptionData.setPath(path);
    }

    public void encryptionFile(String savePath,byte[] bytes,String name) {
        encryptionData.setAuthoer(preference.getString("userPhone", ""));
        File file = new File(encryptionData.getPath());
        savePath = savePath + File.separator + name + ".hgd";
        String finalSavePath = savePath;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (String phone : user) {
                    encryptionData.getKeys().put(phone, phone);
                }
                try {
                    encryptionData.setData(bytes);
                    (new File(finalSavePath)).createNewFile();
                    FileOutputStream fos = new FileOutputStream(finalSavePath);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(encryptionData);
                    oos.close();
                    if (processListner != null) {
                        processListner.finish(finalSavePath, true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (processListner != null) {
                        processListner.finish(finalSavePath, false);
                    }
                } finally {
                    encryptionData = null;
                }
            }
        }).start();

    }

    public void encryptionFile(String savePath,String name) {
        encryptionData.setAuthoer(preference.getString("userPhone", ""));
        File file = new File(encryptionData.getPath());
        savePath = savePath + File.separator + name + ".hgd";
        String finalSavePath = savePath;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (String phone : user) {
                    encryptionData.getKeys().put(phone, phone);
                }
                try {
                    File file = new File(encryptionData.getPath());
                    FileInputStream fis = new FileInputStream(file);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
                    byte[] b = new byte[1000];
                    int n;
                    while ((n = fis.read(b)) != -1) {
                        bos.write(b, 0, n);
                    }
                    fis.close();
                    bos.close();
                    encryptionData.setData(bos.toByteArray());
                    (new File(finalSavePath)).createNewFile();
                    FileOutputStream fos = new FileOutputStream(finalSavePath);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(encryptionData);
                    oos.close();
                    if (processListner != null) {
                        processListner.finish(finalSavePath, true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (processListner != null) {
                        processListner.finish(finalSavePath, false);
                    }
                } finally {
                    encryptionData = null;
                }
            }
        }).start();

    }
    public EncryptionData decryptFile2Data(String formPath) {
        try {
            FileInputStream fis = new FileInputStream(formPath);
            ObjectInputStream ois = new ObjectInputStream(fis);
            encryptionData = (EncryptionData) ois.readObject();
            if (preference.getString("userPhone", "").equals(encryptionData.getAuthoer()) || encryptionData.getKeys().get(preference.getString("userPhone", "")) != null) {
                return encryptionData;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            encryptionData = null;
        }
        return null;
    }

    public byte[] decryptFile(String formPath) {
        try {
            FileInputStream fis = new FileInputStream(formPath);
            ObjectInputStream ois = new ObjectInputStream(fis);
            encryptionData = (EncryptionData) ois.readObject();
            if (preference.getString("userPhone", "").equals(encryptionData.getAuthoer()) || encryptionData.getKeys().get(preference.getString("userPhone", "")) != null) {
                return encryptionData.getData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            encryptionData = null;
        }
        return null;
    }

    public void decryptFile(String formPath, String toPath) {
        File toFile = new File(toPath);
        if (!toFile.exists()) {
            toFile.mkdirs();
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FileInputStream fis = new FileInputStream(formPath);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    encryptionData = (EncryptionData) ois.readObject();
                    String gs=new File(encryptionData.getPath()).getName();
                    String name=new File(formPath).getName();
                    name=name.substring(0,name.lastIndexOf("."))+gs.substring(gs.lastIndexOf("."));

                    if (preference.getString("userPhone", "").equals(encryptionData.getAuthoer()) || encryptionData.getKeys().get(preference.getString("userPhone", "")) != null) {
                        write(toPath, name);
                        if (processListner != null) {
                            processListner.finish(toPath + File.separator + name, true);
                        }
                    } else {
                        if (processListner != null) {
                            processListner.finish("无权开启", false);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    if (processListner != null) {

                        processListner.finish("", false);
                    }
                } finally {
                    encryptionData = null;
                }

            }
        }).start();

    }


    private void write(String toPath, String name) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(toPath);
            if (!dir.exists() && dir.isDirectory()) {//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(toPath + File.separator + name);
            file.createNewFile();
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(encryptionData.getData());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            encryptionData = null;

        }
    }

    public void setProcessListner(ProcessListner processListner) {
        this.processListner = processListner;
    }

    public interface ProcessListner {
        void finish(String path, boolean done);
    }
}
