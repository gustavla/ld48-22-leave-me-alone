import sys

w, h = None, None
m = {}
lines = sys.stdin.readlines()

h = 0
for line in lines:
    if line.rstrip() == '-':
        break
    h += 1

settings_raw = lines[h+1:]
settings = {}

for s in settings_raw:
    vs = s.split(":")
    settings[int(vs[0])] = vs[1].rstrip()

w = len(lines[0])/2


texs = {
    ' ': (True, 'getMainWalkableTex()'),
    '#': (False, 'getMainWallTex()'),
    'o': (True, 2),
    'H': (False, 10),
    'l': (False, 42),
    'r': (False, 43),
    'e': (False, 44),
    'd': (False, 45),
    'D': (True, (10, 11)),
    'C': (False, 12),
    'T': (False, 1),
    'c': (True, (1, 11)),
    '.': (True, 6),
    '%': (False, 15),
    '*': (False, 7),
    'n': (False, 40),
    'L': (False, 41),
}

def bool2str(x):
    return ["false", "true"][int(x)]

def print_unit(unit, x, y):
    print "{0} unit = new {0}(engine); unit.setTilePos({1}, {2}); addEntity(unit);".format(unit, x, y),

def print_item(unit, x, y):
    print "{0} unit = new {0}(engine); unit.setTilePos({1}, {2}); addItem(unit);".format(unit, x, y),

transport_count = 0

print "// Generated by Python. I'm a bit new to Java, so I don't want to start parsing in Java" 
print "super(engine, {0}, {1});".format(w, h)
for j in xrange(h):
    for i in xrange(w):
        print "{",
        print "Tile t = getTile({0}, {1});".format(i, j),
        walkable, tex = texs[lines[j][i*2]]
        if isinstance(tex, tuple):
            print "t.tex = {0};".format(tex[0]),
            print "t.tex2 = {0};".format(tex[1]),
        else:
            print "t.tex = {0};".format(tex),

        print "t.walkable = {0};".format(bool2str(walkable)),
        
        extra = lines[j][i*2+1]
        if '0' <= extra <= '9':
            print "t.transport_to_map = {0};".format(extra)
            print "t.transport_to_tile_x = {0}; t.transport_to_tile_y = {1}; t.transport_facing = {2};".format(*map(int, settings[transport_count].split())),
            transport_count += 1
        elif extra =='N':
            print_unit("NPC", i, j)
        elif extra == 'S': # stone
            print_unit('Stone', i, j) 
        elif extra == 'I': # good inventory item
            print_item('Weapon', i, j)
            print "unit.setType(4);",
        elif extra == 'b': # barrel 
            print_unit('Barrel', i, j)

        print "}"
        
