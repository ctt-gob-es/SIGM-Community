(function ($) {
    "use strict";
    $.fn.bootstrapCaptcha = function (userOptions) {
        var that;
        this.attr('data-valid', 'false');
        this.attr('data-mouseUsed', 'false');
        this.iconSize = '3x';
        this.resetInvalidDrop = false;
        this.clearWhenCorrect = true;
        this.textPrompt = true;
        this.displayTargetArrows = true;
        that = $.extend(true, this, userOptions);
        if (that.onDrop && typeof that.onDrop === 'function') {
            that.callback = true;
        } else {
            that.callback = false;
        }
        that.icons = [
                'envelope',
                'anchor',
                'pencil',
                'bullhorn',
                'fire-extinguisher',
                'camera',
                'wrench',
                'cut',
                'beaker',
                'magic',
                'heart',
                'cogs',
                'trophy',
                'fire',
                'bell',
                'money',
                'truck',
                'coffee',
                'lightbulb',
                'paper-clip',
                'lock',
                'credit-card',
                'headphones',
                'microphone',
                'rocket',
                'fighter-jet',
                'search',
                'beer',
                'eye-open',
                'magnet',
                'ambulance',
                'home',
                'glass',
                'facetime-video',
                'thumbs-up',
                'gift',
                'book',
                'road',
                'star',
                'music',
                'user',
                'shield',
                'puzzle-piece',
                'bolt',
                'briefcase',
                'globe',
                'leaf',
                'circle-blank',
                'calendar',
                'frown',
                'question',
                'print',
                'check',
                'smile',
                'key',
                'keyboard'
        ];
        that.iconNames = {
            envelope: 'envelope',
            anchor: 'anchor',
            pencil: 'pencil',
            bullhorn: 'bullhorn',
            'fire-extinguisher': 'fire extinguisher',
            camera: 'camera',
            wrench: 'wrench',
            cut: 'scissors',
            beaker: 'beaker',
            magic: 'magic wand',
            heart: 'heart',
            cogs: 'cogs',
            trophy: 'trophy',
            fire: 'fire',
            bell: 'bell',
            money: 'money',
            truck: 'truck',
            coffee: 'coffee cup',
            lightbulb: 'lightbulb',
            'paper-clip': 'paper clip',
            lock: 'lock',
            'credit-card': 'credit card',
            headphones: 'headphones',
            microphone: 'microphone',
            smile: 'smily face',
            rocket: 'rocket',
            'fighter-jet': 'fighter jet',
            search: 'magnifying glass',
            beer: 'beer mug',
            'eye-open': 'eye',
            magnet: 'magnet',
            ambulance: 'ambulance',
            home: 'house',
            glass: 'glass',
            'facetime-video': 'movie camera',
            'thumbs-up': 'thumbs-up',
            gift: 'gift',
            book: 'book',
            keyboard: 'keyboard',
            road: 'road',
            star: 'star',
            music: 'musical notes',
            user: 'person',
            shield: 'shield',
            'puzzle-piece': 'puzzle piece',
            bolt: 'lightning bolt',
            question: 'question mark',
            briefcase: 'briefcase',
            globe: 'globe',
            leaf: 'leaf',
            'circle-blank': 'circle',
            calendar: 'calendar',
            check: 'check mark',
            frown: 'frown',
            key: 'key',
            print: 'printer'
        };
        that.used = [];
        that.storedIcons = [];
        that.mouseUsed = false;
        that.bsValid = '';
        that.str = '';
        that.validate = function ($icon) {
            var klass = 'icon-' + that.bsValid,
                x = {
                    valid: false,
                    mouseUsed: false
                };
            that.attr('data-valid', 'false');
            that.attr('data-mouseUsed', 'false');
            if (!that.mouseUsed) {
                if (that.callback === true) {
                    that.onDrop(x);
                }
                return;
            }
            x.mouseUsed = true;
            that.attr('data-mouseUsed', 'true');
            if ($icon.hasClass(klass)) {
                that.attr('data-valid', 'true');
                x.valid = true;
                $('#bsBoop').append($('<i/>', {
                    'class': 'hide valid-icon icon-' + that.iconSize + ' icon-' + that.bsValid,
                    id: 'bsValidIcon'
                }));
                $icon.hide();
                $('#bsTargetSpan').empty().append('<strong>Correct!</strong>');
                $('.icon-bullseye').fadeOut(function () {
                    $('#bsCaptchaTarget').removeClass('alert-danger').addClass('alert-success');
                    $('#bsValidIcon').fadeIn();
                });
                if (that.clearWhenCorrect === true) {
                    $('.bsCaptchaRemove').slideUp();
                }
                if (that.callback === true) {
                    that.onDrop(x);
                }
                return;
            }
            if (that.resetInvalidDrop === true) {
                that.makeLayout();
                return;
            }
            $('#bsCaptchaError').empty().append('Try again');
            if (that.callback === true) {
                that.onDrop(x);
            }
        };
        that.getRandomInt = function (min, max) {
            return Math.floor(Math.random() * (max - min + 1)) + min;
        };
        that.addIcon = function () {
            var randomnumber = that.getRandomInt(0, (that.icons.length - 1));
            if ($.inArray(randomnumber, that.used) === -1) {
                that.used.push(randomnumber);
                $('<i/>', {
                    'class': 'bsDraggable icon-' + that.iconSize + ' icon-' + that.icons[randomnumber]
                }).appendTo('#bsCaptchaOut');
                $('#bsCaptchaOut').append('&nbsp;&nbsp;');
                that.storedIcons.push(that.icons[randomnumber]);
            }
            if (that.used.length < 6) {
                that.addIcon();
                return;
            }
            randomnumber = that.getRandomInt(0, (that.storedIcons.length - 1));
            if (that.textPrompt === true) {
                $('#bsWhat').empty().append('<strong>' + that.iconNames[that.storedIcons[randomnumber]] + '</strong> icon');
            } else {
                $('#bsWhat').empty().append($('<i/>', {
                    'class': 'icon-' + that.iconSize + ' icon-' + that.storedIcons[randomnumber]
                }));
                $('#bsWhat').append(' icon ');
            }
            $('#bsCaptchaTarget').append($('<li/>', {
                id: 'bsBoop'
            }));

            that.bsValid = that.storedIcons[randomnumber];
            $('.bsDraggable').draggable({
                revert: true,
                cursor: "move",
                helper: "clone"
            }).on("mousedown", function () {
                that.mouseUsed = true;
            });
            $('#bsCaptchaTarget').droppable({
                accept: ".bsDraggable",
                activeClass: "ui-state-highlight",
                drop: function (event, ui) {
                    that.validate(ui.draggable);
                }
            });
            $('#bootstrapCaptchaDiv').slideDown();

        };
        //  credit: http://sedition.com/perl/javascript-fy.html
        that.fisherYates = function () {
            var i = that.icons.length,
                j, temp;
            if (i === 0) {
                return false;
            }
            while (--i) {
                j = Math.floor(Math.random() * (i + 1));
                temp = that.icons[i];
                that.icons[i] = that.icons[j];
                that.icons[j] = temp;
            }
            that.addIcon();
        };
        that.makeLayout = function () {
            that.used = [];
            that.storedIcons = [];
            that.mouseUsed = false;
            that.bsValid = '';
            that.str = '';
            that.empty();
            $('<div/>', {
                'class': 'hide',
                id: "bootstrapCaptchaDiv"
            }).appendTo(that);
            $('<div/>', {
                'class': 'row-fluid bsCaptchaDiv bsCaptchaRemove'
            }).appendTo('#bootstrapCaptchaDiv');
            $('<ul/>', {
                'class': 'text-center span6'
            }).appendTo('.bsCaptchaDiv:last').append('<li>Please drag and drop the <span id="bsWhat"></span> below:</li>');
            $('<div/>', {
                'class': "row-fluid bsCaptchaDiv bsCaptchaRemove"
            }).appendTo('#bootstrapCaptchaDiv');
            $('<ul/>', {
                'class': "span6 text-center"
            }).appendTo('.bsCaptchaDiv:last').append('<li id="bsCaptchaOut"></li>');
            $('<div/>', {
                'class': 'row-fluid bsCaptchaRemove'
            }).appendTo('#bootstrapCaptchaDiv').append($('<p/>', {
                'class': 'span6',
                html: '<hr>'
            }));
            $('<div/>', {
                'class': 'row-fluid bsCaptchaDiv'
            }).appendTo('#bootstrapCaptchaDiv');
            $('<ul/>', {
                'class': "span6 text-center"
            }).appendTo('.bsCaptchaDiv:last').append('<li id="bsTargetSpanLI">&nbsp</li>');
            that.str = '<span id="bsTargetSpan">';
            if (that.displayTargetArrows) {
                that.str += '<i class="icon-arrow-down icon-' + that.iconSize + '"></i>';
                that.str += '&nbsp;<strong><span id="bsCaptchaError">to this target</span></strong>&nbsp;';
                that.str += '<i class="icon-arrow-down icon-' + that.iconSize + '"></i>';
            } else {
                that.str += '<strong><span id="bsCaptchaError">to that target</span></strong>';
            }
            that.str += '</span>';
            $('#bsTargetSpanLI').append(that.str);
            $('<div/>', {
                'class': 'row-fluid bsCaptchaDiv'
            }).appendTo('#bootstrapCaptchaDiv');
            $('<ul/>', {
                'class': "span2 offset2 text-center well well-small alert alert-danger",
                id: 'bsCaptchaTarget'
            }).appendTo('.bsCaptchaDiv:last').append('<li><i class="icon-bullseye icon-' + that.iconSize + '"></i></li>');
            that.fisherYates();
        };
        if ($('body').hasClass('bootstrapCaptcha')) {
            return this;
        }
        $('body').addClass('bootstrapCaptcha');
        this.makeLayout();
        return this;
    };
}(jQuery));
