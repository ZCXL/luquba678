package com.zhuchao.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by zhuchao on 8/16/15.
 */
public class ImageBeauty {
    /**
     * 转换成圆角
     *
     * @param bmp
     * @param roundPx
     * @return
     */
    public static Bitmap convertToRoundedCorner(Bitmap bmp, float roundPx) {
        Bitmap newBmp = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);
        // 得到画布
        Canvas canvas = new Canvas(newBmp);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // 第二个和第三个参数一样则画的是正圆的一角，否则是椭圆的一角
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bmp, rect, rect, paint);
        return newBmp;
    }
    /**
     * 将彩色图转换为黑白图
     *
     * @param bmp
     * @return 返回转换好的位图
     */
    public static Bitmap convertToBlackWhite(Bitmap bmp) {
        int width = bmp.getWidth(); // 获取位图的宽
        int height = bmp.getHeight(); // 获取位图的高
        int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];
                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);
                grey = (int) (red * 0.3 + green * 0.59 + blue * 0.11);
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
        }
        Bitmap newBmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        newBmp.setPixels(pixels, 0, width, 0, 0, width, height);
        return newBmp;
    }
    /**
     * 高斯模糊
     *
     * @param bmp
     * @return
     */
    public static Bitmap convertToBlur(Bitmap bmp) {
        // 高斯矩阵
        int[] gauss = new int[] { 1, 2, 1, 2, 4, 2, 1, 2, 1 };
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap newBmp = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int pixColor = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int delta = 16; // 值越小图片会越亮，越大则越暗
        int idx = 0;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                idx = 0;
                for (int m = -1; m <= 1; m++) {
                    for (int n = -1; n <= 1; n++) {
                        pixColor = pixels[(i + m) * width + k + n];
                        pixR = Color.red(pixColor);
                        pixG = Color.green(pixColor);
                        pixB = Color.blue(pixColor);
                        newR = newR + pixR * gauss[idx];
                        newG = newG + pixG * gauss[idx];
                        newB = newB + pixB * gauss[idx];
                        idx++;
                    }
                }
                newR /= delta;
                newG /= delta;
                newB /= delta;
                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));
                pixels[i * width + k] = Color.argb(255, newR, newG, newB);
                newR = 0;
                newG = 0;
                newB = 0;
            }
        }
        newBmp.setPixels(pixels, 0, width, 0, 0, width, height);
        return newBmp;
    }

    /**
     * 素描效果
     *
     * @param bmp
     * @return
     */
    public static Bitmap convertToSketch(Bitmap bmp) {
        int pos, row, col, clr;
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixSrc = new int[width * height];
        int[] pixNvt = new int[width * height];
        // 先对图象的像素处理成灰度颜色后再取反
        bmp.getPixels(pixSrc, 0, width, 0, 0, width, height);
        for (row = 0; row < height; row++) {
            for (col = 0; col < width; col++) {
                pos = row * width + col;
                pixSrc[pos] = (Color.red(pixSrc[pos])
                        + Color.green(pixSrc[pos]) + Color.blue(pixSrc[pos])) / 3;
                pixNvt[pos] = 255 - pixSrc[pos];
            }
        }
        // 对取反的像素进行高斯模糊, 强度可以设置，暂定为5.0
        gaussGray(pixNvt, 5.0, 5.0, width, height);
        // 灰度颜色和模糊后像素进行差值运算
        for (row = 0; row < height; row++) {
            for (col = 0; col < width; col++) {
                pos = row * width + col;
                clr = pixSrc[pos] << 8;
                clr /= 256 - pixNvt[pos];
                clr = Math.min(clr, 255);
                pixSrc[pos] = Color.rgb(clr, clr, clr);
            }
        }
        try {
            bmp.setPixels(pixSrc, 0, width, 0, 0, width, height);
        }catch (IllegalStateException e){
            e.printStackTrace();
        }
        return bmp;
    }
    private static int gaussGray(int[] psrc, double horz, double vert,
                                 int width, int height) {
        int[] dst, src;
        double[] n_p, n_m, d_p, d_m, bd_p, bd_m;
        double[] val_p, val_m;
        int i, j, t, k, row, col, terms;
        int[] initial_p, initial_m;
        double std_dev;
        int row_stride = width;
        int max_len = Math.max(width, height);
        int sp_p_idx, sp_m_idx, vp_idx, vm_idx;
        val_p = new double[max_len];
        val_m = new double[max_len];
        n_p = new double[5];
        n_m = new double[5];
        d_p = new double[5];
        d_m = new double[5];
        bd_p = new double[5];
        bd_m = new double[5];
        src = new int[max_len];
        dst = new int[max_len];
        initial_p = new int[4];
        initial_m = new int[4];
        // 垂直方向
        if (vert > 0.0) {
            vert = Math.abs(vert) + 1.0;
            std_dev = Math.sqrt(-(vert * vert) / (2 * Math.log(1.0 / 255.0)));
            // 初试化常量
            findConstants(n_p, n_m, d_p, d_m, bd_p, bd_m, std_dev);
            for (col = 0; col < width; col++) {
                for (k = 0; k < max_len; k++) {
                    val_m[k] = val_p[k] = 0;
                }
                for (t = 0; t < height; t++) {
                    src[t] = psrc[t * row_stride + col];
                }
                sp_p_idx = 0;
                sp_m_idx = height - 1;
                vp_idx = 0;
                vm_idx = height - 1;
                initial_p[0] = src[0];
                initial_m[0] = src[height - 1];
                for (row = 0; row < height; row++) {
                    terms = (row < 4) ? row : 4;
                    for (i = 0; i <= terms; i++) {
                        val_p[vp_idx] += n_p[i] * src[sp_p_idx - i] - d_p[i]
                                * val_p[vp_idx - i];
                        val_m[vm_idx] += n_m[i] * src[sp_m_idx + i] - d_m[i]
                                * val_m[vm_idx + i];
                    }
                    for (j = i; j <= 4; j++) {
                        val_p[vp_idx] += (n_p[j] - bd_p[j]) * initial_p[0];
                        val_m[vm_idx] += (n_m[j] - bd_m[j]) * initial_m[0];
                    }
                    sp_p_idx++;
                    sp_m_idx--;
                    vp_idx++;
                    vm_idx--;
                }
                transferGaussPixels(val_p, val_m, dst, 1, height);
                for (t = 0; t < height; t++) {
                    psrc[t * row_stride + col] = dst[t];
                }
            }
        }
        // 水平方向
        if (horz > 0.0) {
            horz = Math.abs(horz) + 1.0;
            if (horz != vert) {
                std_dev = Math.sqrt(-(horz * horz)
                        / (2 * Math.log(1.0 / 255.0)));
                // 初试化常量
                findConstants(n_p, n_m, d_p, d_m, bd_p, bd_m, std_dev);
            }
            for (row = 0; row < height; row++) {
                for (k = 0; k < max_len; k++) {
                    val_m[k] = val_p[k] = 0;
                }
                for (t = 0; t < width; t++) {
                    src[t] = psrc[row * row_stride + t];
                }
                sp_p_idx = 0;
                sp_m_idx = width - 1;
                vp_idx = 0;
                vm_idx = width - 1;
                initial_p[0] = src[0];
                initial_m[0] = src[width - 1];
                for (col = 0; col < width; col++) {
                    terms = (col < 4) ? col : 4;
                    for (i = 0; i <= terms; i++) {
                        val_p[vp_idx] += n_p[i] * src[sp_p_idx - i] - d_p[i]
                                * val_p[vp_idx - i];
                        val_m[vm_idx] += n_m[i] * src[sp_m_idx + i] - d_m[i]
                                * val_m[vm_idx + i];
                    }
                    for (j = i; j <= 4; j++) {
                        val_p[vp_idx] += (n_p[j] - bd_p[j]) * initial_p[0];
                        val_m[vm_idx] += (n_m[j] - bd_m[j]) * initial_m[0];
                    }
                    sp_p_idx++;
                    sp_m_idx--;
                    vp_idx++;
                    vm_idx--;
                }
                transferGaussPixels(val_p, val_m, dst, 1, width);
                for (t = 0; t < width; t++) {
                    psrc[row * row_stride + t] = dst[t];
                }
            }
        }
        return 0;
    }
    private static void transferGaussPixels(double[] src1, double[] src2, int[] dest, int bytes, int width) {
        int i, j, k, b;
        int bend = bytes * width;
        double sum;
        i = j = k = 0;
        for (b = 0; b < bend; b++) {
            sum = src1[i++] + src2[j++];
            if (sum > 255)
                sum = 255;
            else if (sum < 0)
                sum = 0;
            dest[k++] = (int) sum;
        }
    }
    private static void findConstants(double[] n_p, double[] n_m, double[] d_p,
                                      double[] d_m, double[] bd_p, double[] bd_m, double std_dev) {
        double div = Math.sqrt(2 * 3.141593) * std_dev;
        double x0 = -1.783 / std_dev;
        double x1 = -1.723 / std_dev;
        double x2 = 0.6318 / std_dev;
        double x3 = 1.997 / std_dev;
        double x4 = 1.6803 / div;
        double x5 = 3.735 / div;
        double x6 = -0.6803 / div;
        double x7 = -0.2598 / div;
        int i;
        n_p[0] = x4 + x6;
        n_p[1] = (Math.exp(x1)
                * (x7 * Math.sin(x3) - (x6 + 2 * x4) * Math.cos(x3)) + Math
                .exp(x0) * (x5 * Math.sin(x2) - (2 * x6 + x4) * Math.cos(x2)));
        n_p[2] = (2
                * Math.exp(x0 + x1)
                * ((x4 + x6) * Math.cos(x3) * Math.cos(x2) - x5 * Math.cos(x3)
                * Math.sin(x2) - x7 * Math.cos(x2) * Math.sin(x3)) + x6
                * Math.exp(2 * x0) + x4 * Math.exp(2 * x1));
        n_p[3] = (Math.exp(x1 + 2 * x0)
                * (x7 * Math.sin(x3) - x6 * Math.cos(x3)) + Math.exp(x0 + 2
                * x1)
                * (x5 * Math.sin(x2) - x4 * Math.cos(x2)));
        n_p[4] = 0.0;
        d_p[0] = 0.0;
        d_p[1] = -2 * Math.exp(x1) * Math.cos(x3) - 2 * Math.exp(x0)
                * Math.cos(x2);
        d_p[2] = 4 * Math.cos(x3) * Math.cos(x2) * Math.exp(x0 + x1)
                + Math.exp(2 * x1) + Math.exp(2 * x0);
        d_p[3] = -2 * Math.cos(x2) * Math.exp(x0 + 2 * x1) - 2 * Math.cos(x3)
                * Math.exp(x1 + 2 * x0);
        d_p[4] = Math.exp(2 * x0 + 2 * x1);
        for (i = 0; i <= 4; i++) {
            d_m[i] = d_p[i];
        }
        n_m[0] = 0.0;
        for (i = 1; i <= 4; i++) {
            n_m[i] = n_p[i] - d_p[i] * n_p[0];
        }
        double sum_n_p, sum_n_m, sum_d;
        double a, b;
        sum_n_p = 0.0;
        sum_n_m = 0.0;
        sum_d = 0.0;
        for (i = 0; i <= 4; i++) {
            sum_n_p += n_p[i];
            sum_n_m += n_m[i];
            sum_d += d_p[i];
        }
        a = sum_n_p / (1.0 + sum_d);
        b = sum_n_m / (1.0 + sum_d);
        for (i = 0; i <= 4; i++) {
            bd_p[i] = d_p[i] * a;
            bd_m[i] = d_m[i] * b;
        }
    }

    /**
     * 图片锐化（拉普拉斯变换）
     *
     * @param bmp
     * @return
     */
    public static Bitmap sharpenImageAmeliorate(Bitmap bmp) {
        // 拉普拉斯矩阵
        int[] laplacian = new int[] { -1, -1, -1, -1, 9, -1, -1, -1, -1 };
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int pixColor = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int idx = 0;
        float alpha = 0.3F;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                idx = 0;
                for (int m = -1; m <= 1; m++) {
                    for (int n = -1; n <= 1; n++) {
                        pixColor = pixels[(i + n) * width + k + m];
                        pixR = Color.red(pixColor);
                        pixG = Color.green(pixColor);
                        pixB = Color.blue(pixColor);
                        newR = newR + (int) (pixR * laplacian[idx] * alpha);
                        newG = newG + (int) (pixG * laplacian[idx] * alpha);
                        newB = newB + (int) (pixB * laplacian[idx] * alpha);
                        idx++;
                    }
                }
                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));
                pixels[i * width + k] = Color.argb(255, newR, newG, newB);
                newR = 0;
                newG = 0;
                newB = 0;
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

}
