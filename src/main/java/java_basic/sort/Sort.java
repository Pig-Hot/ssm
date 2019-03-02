package java_basic.sort;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Created by zhuran on 2019/2/26 0026
 */
public class Sort {
    //插入
    private static void insertSort(int[] a) {
        int len = a.length;//单独把数组长度拿出来，提高效率
        int insertNum;//要插入的数
        for (int i = 1; i < len; i++) {//因为第一次不用，所以从1开始
            insertNum = a[i];
            int j = i - 1;//序列元素个数
            while (j >= 0 && a[j] > insertNum) {//从后往前循环，将大于insertNum的数向后移动
                a[j + 1] = a[j];//元素向后移动
                j--;
            }
            a[j + 1] = insertNum;//找到位置，插入当前元素
        }
    }

    //希尔
    private static void shellSort(int[] a) {
        int len = a.length;//单独把数组长度拿出来，提高效率
        while (len != 0) {
            len = len / 2;
            for (int i = 0; i < len; i++) {//分组
                for (int j = i + len; j < a.length; j += len) {//元素从第二个开始
                    int k = j - len;//k为有序序列最后一位的位数
                    int temp = a[j];//要插入的元素
                    /*for(;k>=0&&temp<a[k];k-=len){
                        a[k+len]=a[k];
                    }*/
                    while (k >= 0 && temp < a[k]) {//从后往前遍历
                        a[k + len] = a[k];
                        k -= len;//向后移动len位
                    }
                    a[k + len] = temp;
                }
            }
        }
    }

    //选择
    private static void selectSort(int[] a) {
        int len = a.length;
        for (int i = 0; i < len; i++) {//循环次数
            int value = a[i];
            int position = i;
            for (int j = i + 1; j < len; j++) {//找到最小的值和位置
                if (a[j] < value) {
                    value = a[j];
                    position = j;
                }
            }
            a[position] = a[i];//进行交换
            a[i] = value;
        }
    }

    //冒泡
    private static void bubbleSort(int[] a) {
        int len = a.length;
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len - i - 1; j++) {//注意第二重循环的条件
                if (a[j] > a[j + 1]) {
                    int temp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = temp;
                }
            }
        }
    }

    //快排
    private static void quickSort(int[] a, int start, int end) {
        if (start < end) {
            int baseNum = a[start];//选基准值
            int midNum;//记录中间值
            int i = start;
            int j = end;
            do {
                while ((a[i] < baseNum) && i < end) {
                    i++;
                }
                while ((a[j] > baseNum) && j > start) {
                    j--;
                }
                if (i <= j) {
                    midNum = a[i];
                    a[i] = a[j];
                    a[j] = midNum;
                    i++;
                    j--;
                }
            } while (i <= j);
            if (start < j) {
                quickSort(a, start, j);
            }
            if (end > i) {
                quickSort(a, i, end);
            }
        }
    }

    public static void heapSort(int[] a) {
        int len = a.length;
        //循环建堆
        for (int i = 0; i < len - 1; i++) {
            //建堆
            buildMaxHeap(a, len - 1 - i);
            //交换堆顶和最后一个元素
            swap(a, 0, len - 1 - i);
        }
    }

    //交换方法
    private static void swap(int[] data, int i, int j) {
        int tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
    }

    //对data数组从0到lastIndex建大顶堆
    private static void buildMaxHeap(int[] data, int lastIndex) {
        //从lastIndex处节点（最后一个节点）的父节点开始
        for (int i = (lastIndex - 1) / 2; i >= 0; i--) {
            //k保存正在判断的节点
            int k = i;
            //如果当前k节点的子节点存在
            while (k * 2 + 1 <= lastIndex) {
                //k节点的左子节点的索引
                int biggerIndex = 2 * k + 1;
                //如果biggerIndex小于lastIndex，即biggerIndex+1代表的k节点的右子节点存在
                if (biggerIndex < lastIndex) {
                    //若果右子节点的值较大
                    if (data[biggerIndex] < data[biggerIndex + 1]) {
                        //biggerIndex总是记录较大子节点的索引
                        biggerIndex++;
                    }
                }
                //如果k节点的值小于其较大的子节点的值
                if (data[k] < data[biggerIndex]) {
                    //交换他们
                    swap(data, k, biggerIndex);
                    //将biggerIndex赋予k，开始while循环的下一次循环，重新保证k节点的值大于其左右子节点的值
                    k = biggerIndex;
                } else {
                    break;
                }
            }
        }
    }

    private static boolean binSearch(int[] array, int key) {
        int base = 0;
        int max = array.length - 1;
        int mid = max / 2;
        while (base < max) {
            if (key > array[mid]) {
                base = mid;
                mid = (base + max) / 2;
            } else if (key < array[mid]) {
                max = mid;
                mid = (base + max) / 2;
            } else {
                System.out.println(mid);
                System.out.println(array[mid]);
                return true;
            }
        }
        return false;
    }

    private static void qucikSort(int[] array, int start, int end) {
        if (start < end) {
            int base = array[start];
            int i = start;
            int j = end;
            int mid;
            while (i != j) {
                while (array[j] > base && i < j) {
                    j--;
                }
                while (array[i] < base && i < j) {
                    i++;
                }
                if (i < j) {
                    mid = array[i];
                    array[i] = array[j];
                    array[j] = mid;
                }
            }
            qucikSort(array, start, i - 1);
            qucikSort(array, i + 1, end);
        }
    }

    public static int[] sort(int[] array, int left, int right) {
        if (left == right) {
            return new int[] { array[left] };
        }
        int mid = (right + left) / 2;
        int[] l = sort(array, left, mid);
        int[] r = sort(array, mid + 1, right);
        return merge(l, r);
    }
// 将两个数组合并成一个
    public static int[] merge(int[] l, int[] r) {
        int[] result = new int[l.length + r.length];
        int p = 0;
        int lp = 0;
        int rp = 0;
        while (lp < l.length && rp < r.length) {
            result[p++] = l[lp] < r[rp] ? l[lp++] : r[rp++];
        }
        while (lp < l.length) {
            result[p++] = l[lp++];
        }
        while (rp < r.length) {
            result[p++] = r[rp++];
        }
        return result;
    }

    public static void main(String[] args) {
        int[] array = new int[]{3, 7, 2, 6, 9, 0, 10, 8, 1};
//        insertSort(array);
//        shellSort(array);
//        selectSort(array);
//        bubbleSort(array);
//        quickSort(array,0,array.length - 1);
        array = sort(array, 0, array.length - 1);
        for (int anArray : array) {
            System.out.print(anArray + " ");
        }
        System.out.println(Arrays.binarySearch(array,6));
        System.out.println(binSearch(array, 6));
        Collections.synchronizedList(Arrays.asList(array));
//        String[] s = new String[]{"a","s","d"};
//        Arrays.stream(s).filter(x -> !x.equals("s")).collect(Collectors.toList());
    }
}
