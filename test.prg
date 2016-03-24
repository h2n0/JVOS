clean;

num x = 0;
num y = 0;
num e = 19;

x += 10;
y += 10;

num z = e + 3;

write "X:";
write x;

write "Y:";
write z;

place 40 40 15;
draw;
pause;

write $DATE;
write $TIME;
write "finished";
draw;