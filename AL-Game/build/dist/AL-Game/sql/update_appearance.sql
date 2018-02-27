ALTER TABLE `player_appearance`
ADD COLUMN `pupil_shape`  int(11) NOT NULL DEFAULT 0 AFTER `height`,
ADD COLUMN `eye_rgb_right`  int(11) NOT NULL DEFAULT 0 AFTER `pupil_shape`,
ADD COLUMN `eye_lash`  int(11) NOT NULL DEFAULT 0 AFTER `eye_rgb_right`,
ADD COLUMN `pupil_size`  int(11) NOT NULL DEFAULT 0 AFTER `eye_lash`,
ADD COLUMN `upper_torso`  int(11) NOT NULL DEFAULT 0 AFTER `pupil_size`,
ADD COLUMN `forearm_thickness`  int(11) NOT NULL DEFAULT 0 AFTER `upper_torso`,
ADD COLUMN `hand_span`  int(11) NOT NULL DEFAULT 0 AFTER `forearm_thickness`,
ADD COLUMN `calf_thickness`  int(11) NOT NULL DEFAULT 0 AFTER `hand_span`;



