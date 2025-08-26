/* fork-example-1.c */

#include <stdio.h>
#include <stdlib.h>
#include <sys/mman.h>
#include <string.h>
#include <unistd.h>

void* create_shared_memory(size_t size) {
  return mmap(NULL,
              size,
              PROT_READ | PROT_WRITE,
              MAP_SHARED | MAP_ANONYMOUS,
              -1, 0);
}


int main() {
    int* shmem = create_shared_memory(sizeof(int) * 10);
    int nums ={1,2,3,4,5,6,7,8,9,10};
    for (size_t i = 0; i < 10; i++) {
        shmem[i] = nums[i];
    }
    printf("Shared memory at: %p\n" , shmem);
    int pid = fork();

    if (pid == 0) {
        size_t index;
        int num;
        scanf("%zu %d", &index, &num);
        if (index < size) {
            arr[index] = new_value;
        }
        exit(0);
    } else {
        wait(NULL);
        printf("Child's pid is: %d\n", pid);
        printf("Массив чисел в общей памяти после изменения:\n");
        for (int i = 0; i < size; i++) {
            printf("%d ", shmem[i]);
        }
        printf("\n");
    }
    return 0;
}
