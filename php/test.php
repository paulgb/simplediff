<?php
/**
 * Adapted from https://github.com/paulgb/simplediff/blob/master/python/test.py
 */
include 'simplediff.php';

$TESTS = array(
    'insert'=> array(
    array(
            "old"=> array(1, 3, 4),
            "new"=> array(1, 2, 3, 4),
            "diff"=> array(
                     array("=", array(1)),
                     array("+", array(2)),
                     array("=", array(3, 4))
                    )
        ),
    array(
            "old"=> array(1, 2, 3, 8, 9, 12, 13),
            "new"=> array(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15),
            "diff"=> array(array("=", array(1, 2, 3)),
                   array("+", array(4, 5, 6, 7)),
                   array("=", array(8, 9)),
                   array("+", array(10, 11)),
                   array("=", array(12, 13)),
                   array("+", array(14, 15)))
        ),
    array(
            "old"=> array(1, 2, 3, 4, 5),
            "new"=> array(1, 2, 2, 3, 4, 5),
            "diff"=> array(array("=", array(1)),
                   array("+", array(2)),
                   array("=", array(2, 3, 4, 5)))
        ),
    array(
            "old"=> array(1, 2, 3, 4, 5),
            "new"=> array(1, 2, 2, 3, 4, 4, 5),
            "diff"=> array(array("=", array(1)),
                   array("+", array(2)),
                   array("=", array(2, 3, 4)),
                   array("+", array(4)),
                   array("=", array(5)))
        ),
    array(
            "old"=> array(1, 2, 3, 4, 5),
            "new"=> array(1, 2, 1, 2, 3, 3, 2, 1, 4, 5),
            "diff"=> array(array("+", array(1, 2)),
                   array("=", array(1, 2, 3)),
                   array("+", array(3, 2, 1)),
                   array("=", array(4, 5)))
        )
    ),
    'delete'=> array(
    array(
            "old"=> array(1, 2, 3, 4, 5),
            "new"=> array(1, 2, 5),
            "diff"=> array(array("=", array(1, 2)),
                   array("-", array(3, 4)),
                   array("=", array(5)))
        ),
    array(
            "old"=> array(1, 2, 3, 4, 5, 6, 7, 8),
            "new"=> array(3, 6, 7),
            "diff"=> array(array("-", array(1, 2)),
                   array("=", array(3)),
                   array("-", array(4, 5)),
                   array("=", array(6, 7)),
                   array("-", array(8)))
        ),
    array(
            "old"=> array(1, 2, 3, 4, 5, 1, 2, 3, 4, 5),
            "new"=> array(1, 2, 3, 4, 5),
            "diff"=> array(array("=", array(1, 2, 3, 4, 5)),
                   array("-", array(1, 2, 3, 4, 5)))
        )
    ),
    'words'=> array(
    array(
            "old"=> array("The", "quick", "brown", "fox"),
            "new"=> array("The", "slow", "green", "turtle"),
            "diff"=> array(array("=", array("The")),
                   array("-", array("quick", "brown", "fox")),
                   array("+", array("slow", "green", "turtle")))
        ),
    array(
            "old"=> array("jumps", "over", "the", "lazy", "dog"),
            "new"=> array("walks", "around", "the", "orange", "cat"),
            "diff"=> array(array("-", array("jumps", "over")),
                   array("+", array("walks", "around")),
                   array("=", array("the")),
                   array("-", array("lazy", "dog")),
                   array("+", array("orange", "cat")))
        )
    ),
    'character'=> array(
     array(
            "old"=>"The quick brown fox.",
            "new"=>"The kuick brown fix.",
            "diff"=> array(array("=", "The "),
                   array("-", "q"),
                   array("+", "k"),
                   array("=", "uick brown f"),
                   array("-", "o"),
                   array("+", "i"),
                   array("=", "x."))
        )
    )
);
?>
<html>
<head>
    <meta charset="utf-8">
    <title>TESTS - simplediff</title>
    <style>
        del{background:#fcc}  ins{background:#cfc} 
        span.res{font-size: 130%; }
    </style>    
</head>

<body>
<pre>
<?php

$MSG = "\n\t\tDIFF(old,new): <span class='res'>";
foreach($TESTS as $k1=>$r1) {
    print "Tests for <b>$k1</b>:";
    foreach($r1 as $k2=>$g)  {
        $k2++;
        print "\n\t<b>group#$k2</b>:";
        if (is_array($g['new']))
            print "\n\t\told=". join('|',$g['old']) 
                ."\n\t\tnew=".join('|',$g['new'])
                .$MSG.htmlDiff($g['old'], $g['new']);
        else
            print "\n\t\told=$g[old]\n\t\tnew=$g[new]"
                .$MSG.htmlDiff($g['old'], $g['new']);
        print "</span>";
    }
    print "\n\n";
}

?>
</pre>
</body>
</html>