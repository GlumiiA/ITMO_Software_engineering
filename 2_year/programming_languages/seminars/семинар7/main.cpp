/* tail-rec.c */

#include <inttypes.h>
#include <malloc.h>
#include <stddef.h>
#include <stdio.h>

void print_size(size_t i) { printf("%zu" , i); }

struct list {
    int64_t elem;
    struct list *next;
};

struct list *c(int64_t head, struct list *tail) {
    struct list *h = (struct list *)malloc(sizeof(struct list));
    h->elem = head;
    h->next = tail;
    return h;
}

size_t list_length_count(struct list const *l, int count) {
    if (!l) return count;
    return list_length_count(l->next, count + 1);
}


size_t list_length(struct list const *l) {
//    size_t length = 0;
//    while (l != NULL) {
//        length++;
//        l = l->next;
//    }
//    return length;
    return list_length_count(l, 0);
}

int main(int argc, char **argv) {
    const size_t len = 1024 * 1024;

    struct list *lst = NULL;

    for( size_t i = 0; i < len; i++) {
        lst = c(i, lst);
    }

    print_size(list_length(lst));
    return 0;
}