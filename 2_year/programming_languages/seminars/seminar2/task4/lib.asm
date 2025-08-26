; Принимает указатель на строку, пытается
; прочитать из её начала беззнаковое число.
; Возвращает в rax: число, rdx : его длину в символах
; rdx = 0 если число прочитать не удалось
parse_uint:
  push rbx
  xor rdx, rdx
  xor rax, rax
  xor rbx, rbx
  .loop:
    mov bl, byte [rdi + rdx]

    sub bl, '0'
    jl .return

    cmp bl, 9
    jg .return

    push rdx
    mov rdx, 10
    mul rdx       ; rax *= 10
    pop rdx

    add rax, rbx  ; rax += rbx

    inc rdx
    jmp .loop

  .return:
    pop rbx
    ret

; Принимает: адрес начала буфера, размер буфера
; Читает в буфер слово из stdin, пропуская пробельные символы в начале, .
; Пробельные символы это пробел 0x20, табуляция 0x9 и перевод строки 0xA.
; Останавливается и возвращает 0 если слово слишком большое для буфера
; При успехе возвращает адрес буфера в rax, длину слова в rdx.
; При неудаче возвращает 0 в rax
; Эта функция должна дописывать к слову нуль-терминатор
read_word:
    push rdi
    xor rax, rax
    xor rdx, rdx  
    xor rdi, rdi
    push rsp
    .loop_spaces:
        call read_char      
        cmp rax, 0           
        je .end
        cmp al, 0x20
        je .loop_spaces
        cmp al, 0x9   
        je .loop_spaces
        cmp al, 0xA
        je .loop_spaces
        inc rcx 
        cmp rcx, rsi          ; проверяем, не превышен ли размер буфера
        jge .word_big
        mov [rsi], al             
        inc rdi             ; Увеличиваем длину слова              
        jmp .loop_spaces

    .end:
        inc rcx 
        cmp rcx, rsi          ; проверяем, не превышен ли размер буфера
        jge .word_big
        mov byte [rsi], 0   ; Добавляем нуль-терминатор
        mov rdx, rdi        ; Возвращаем длину слова
        pop rdi  
        mov rax, rdi
        pop rsp
        ret
    .word_big:
        xor rax, rax  
        pop rsp     
        pop rdi  
        ret
