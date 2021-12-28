f = open("data.txt")
newf = open("rightdata.txt", "a")

tol = 0
delete_num = 0
for line in f:
    tol += 1

    str_list = line.split()
    # 判断字段是否完全
    if len(str_list) != 14:
        delete_num += 1
        print(str_list)
        continue

    # 判断时间是否有效
    if int(str_list[11]) <= 0:
        delete_num += 1
        print(str_list)
        continue

    newf.write(line)
print(tol)
print(delete_num)
